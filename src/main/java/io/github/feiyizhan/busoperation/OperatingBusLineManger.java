package io.github.feiyizhan.busoperation;

import io.github.feiyizhan.busoperation.constant.SystemConstant;
import io.github.feiyizhan.busoperation.enums.BusLogTypeEnum;
import io.github.feiyizhan.busoperation.enums.BusOperatingInLineStateEnum;
import io.github.feiyizhan.busoperation.pojo.*;
import io.github.feiyizhan.busoperation.utils.CustomRandomUtils;
import io.github.feiyizhan.busoperation.utils.PassengerBuilder;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 运营公交线路管理者
 * @author 徐明龙 XuMingLong 2021-06-23
 */
public class OperatingBusLineManger {

    /**
     * 乘客的定时处理任务
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private ScheduledExecutorService passengerScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    /**
     * 上行线路定时处理任务
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private ScheduledExecutorService upLineScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    /**
     * 下行线路定时处理任务
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private ScheduledExecutorService downLineScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    /**
     * 公交运行的定时处理任务
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private ScheduledExecutorService busRunScheduledExecutorService ;

    /**
     * 运营的公交线路
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private OperatingBusLine operatingBusLine;

    /**
     * 上行运行的公交车集合
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private Map<Integer, OperatingBus> upOperatingBusMap ;

    /**
     * 下行运行的公交车集合
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private Map<Integer, OperatingBus> downOperatingBusMap ;

    /**
     * 待运营的公交车列表
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private BlockingQueue<OperatingBus> pendingToRunBusList ;

    /**
     *  运营标志
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private AtomicBoolean runFlag = new AtomicBoolean(true);

    /**
     * 乘客id
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private AtomicInteger passengerId = new AtomicInteger();

    /**
     * 开始时间
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private LocalDateTime begin ;

    /**
     * 公交日志
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private Map<Integer,List<BusLog>> busLogMap;


    /**
     * 构建一个线路运营者
     * @author 徐明龙 XuMingLong 2021-06-23
     * @param operatingBusLine
     * @return
     */
    public OperatingBusLineManger(OperatingBusLine operatingBusLine){
        this.operatingBusLine = operatingBusLine;
        int busSize = operatingBusLine.getOperatingBusMap().size();
        this.upOperatingBusMap = new ConcurrentHashMap<>(busSize);
        this.downOperatingBusMap = new ConcurrentHashMap<>(busSize);
        this.pendingToRunBusList = new ArrayBlockingQueue<>(busSize);
        //初始化待运营的车辆列表
        operatingBusLine.getOperatingBusMap().forEach((k,v)->{
            pendingToRunBusList.add(v);
        });

        //构建公交车运行的线程池
        busRunScheduledExecutorService = new ScheduledThreadPoolExecutor(busSize);
        //公交日志记录
        busLogMap = new ConcurrentHashMap<>(busSize);

    }

    /**
     * 线路运行
     * @author 徐明龙 XuMingLong 2021-06-23
     * @param time
     * @param timeUnit
     * @return void
     */
    public void run(long time, TimeUnit timeUnit){
        begin = LocalDateTime.now();
        //启动定时任务，每5分钟增加10名乘客
        passengerScheduledExecutorService.scheduleAtFixedRate(()->addPassenger(),
            0L,5L,timeUnit);

        //启动定时任务，每15分钟两边各发出一辆公交车
        upLineScheduledExecutorService.scheduleAtFixedRate(()->addOperatingBus(BusOperatingInLineStateEnum.UP),
            0L,15L,timeUnit);
        downLineScheduledExecutorService.scheduleAtFixedRate(()->addOperatingBus(BusOperatingInLineStateEnum.DOWN),
            0L,15L,timeUnit);

        //启动结束指令，300分钟后结束
        ScheduledExecutorService mainScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        ScheduledFuture<?> future = mainScheduledExecutorService.schedule(()->exit(),
            time, timeUnit);
        try {
            future.get();
            mainScheduledExecutorService.shutdownNow();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //等待所有线程都执行完毕
        while (true){
            if(busRunScheduledExecutorService.isTerminated()){
                break;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //输出日志
        printLog();

        System.out.println(Duration.between(begin, LocalDateTime.now()).toSeconds());

    }


    /**
     * 添加乘客到站点
     * @author 徐明龙 XuMingLong 2021-06-23
     * @return void
     */
    private void addPassenger(){
        if(!runFlag.get()){
            //结束运营
            passengerScheduledExecutorService.shutdownNow();
            return;
        }
        //每次添加10名乘客到站点的队列
        for(int i=0;i<10;i++){
            Passenger passenger = PassengerBuilder.build(passengerId.getAndIncrement(),operatingBusLine);
//            System.out.println("获取到乘客:"+passenger);
            Map<Integer, OperatingBusStop> operatingBusStopMap = operatingBusLine.getOperatingBusStopMap();
            OperatingBusStop busStop = operatingBusStopMap.get(passenger.getFromStop());
            try {
                //判断乘客是走上行还是走下行
                if(passenger.getFromStop()<passenger.getDestinationStop()){
                    busStop.getWaitingInUpPassengers().put(passenger);
                }else{
                    busStop.getWaitingInDownPassengers().put(passenger);
                }
                System.out.println("添加乘客到队列成功:"+passenger);
            } catch (InterruptedException e) {
                System.out.println("添加乘客失败");
                e.printStackTrace();
            }
        }
    }

    /**
     * 增加运营的公交车
     * @author 徐明龙 XuMingLong 2021-06-23
     * @param inLineStateEnum 运行线路
     * @return void
     */
    private void addOperatingBus(BusOperatingInLineStateEnum inLineStateEnum){
        if(!runFlag.get()){
            //结束运营
            upLineScheduledExecutorService.shutdownNow();
            downLineScheduledExecutorService.shutdownNow();
            return;
        }
        OperatingBus bus = pendingToRunBusList.poll();
        if(bus==null){
            upLineScheduledExecutorService.shutdownNow();
            downLineScheduledExecutorService.shutdownNow();
        }
        bus.setInLineState(inLineStateEnum);
        switch (inLineStateEnum){
            case UP:
                bus.setCurrentStopId(operatingBusLine.getFirstStop().getId());
                bus.setNextStopId(operatingBusLine.getFirstStop().getIdOfNextStop());
                upOperatingBusMap.put(bus.getId(),bus);
                //执行公交车调度
                processByBus(bus);
                System.out.println("添加上行BUS" + bus.getName());
                break;
            case DOWN:
                bus.setCurrentStopId(operatingBusLine.getLastStop().getId());
                bus.setNextStopId(operatingBusLine.getLastStop().getId());
                downOperatingBusMap.put(bus.getId(),bus);
                //执行公交车调度
                processByBus(bus);
                System.out.println("添加下行BUS" + bus.getName());
                break;

        }
    }


    /**
     * 公交调度运行
     * @author 徐明龙 XuMingLong 2021-06-23
     * @param bus
     * @return void
     */
    private void processByBus(OperatingBus bus){
        LocalDateTime now = LocalDateTime.now();
        //更新总行驶时间
        long totalDurationOfDriving = bus.getTotalDurationOfDriving();
        bus.setTotalDurationOfDriving(totalDurationOfDriving+bus.getDurationOfToNextStop());
        long totalDurationOfOperating = bus.getTotalDurationOfOperating();
        //运行时间累计行驶耗时
        totalDurationOfOperating +=bus.getDurationOfToNextStop();


        //判断是否为终点站
        boolean isLastStop = isLastStop(bus);
        //执行公交进站处理
        processByBusStop(bus,now,isLastStop);
        //运行时间累加乘客耗时
        totalDurationOfOperating+=bus.getCurrentDurationOfPassengers();
        //更新运行时间,
        bus.setTotalDurationOfOperating(totalDurationOfOperating);

        //当前时间更新，增加乘客耗时
        now = now.plusSeconds(bus.getCurrentDurationOfPassengers());
        //执行公交离站处理
        processByBusDeparts(bus,now,isLastStop);


        if(!runFlag.get()){
            //结束运营
            busRunScheduledExecutorService.shutdown();
            return;
        }
        //启动公交车的下一次调度
        busRunScheduledExecutorService.schedule(()->processByBus(bus),
            bus.getDurationOfToNextStop(),TimeUnit.SECONDS);
    }

    /**
     * 判断是否为终点站
     * @author 徐明龙 XuMingLong 2021-06-23
     * @param bus
     * @return boolean
     */
    private boolean isLastStop(OperatingBus bus){
        //获取到当前站点
        OperatingBusStop currentStop = operatingBusLine.getOperatingBusStopMap().get(bus.getCurrentStopId());
        //获取当前线路方向
        BusOperatingInLineStateEnum inLineState = bus.getInLineState();
        switch (inLineState){
            case UP:
                return currentStop.getIdOfNextStop()==null;
            case DOWN:
                return currentStop.getIdOfPreviousStop()==null;
        }
        return false;
    }

    /**
     * 是否为始发站
     * @author 徐明龙 XuMingLong 2021-06-23
     * @param bus
     * @return boolean
     */
    private boolean isFirstStop(OperatingBus bus){
        //获取到当前站点
        OperatingBusStop currentStop = operatingBusLine.getOperatingBusStopMap().get(bus.getCurrentStopId());
        //获取当前线路方向
        BusOperatingInLineStateEnum inLineState = bus.getInLineState();
        switch (inLineState){
            case UP:
                return currentStop.getIdOfPreviousStop()==null;
            case DOWN:
                return currentStop.getIdOfNextStop()==null;
        }
        return false;
    }



    /**
     * 公交进站处理，
     * @author 徐明龙 XuMingLong 2021-06-23
     * @param bus
     * @param now
     * @param isLastStop
     * @return void
     */
    private void processByBusStop(OperatingBus bus, LocalDateTime now, boolean isLastStop){
        //获取到当前站点
        OperatingBusStop currentBusStop = operatingBusLine.getOperatingBusStopMap().get(bus.getCurrentStopId());
        //先执行人员下车
        long durationOfPassengers = 0L;
        int reducePassengersNumber = 0;
        if(bus.getCurrentPassengersNumber()!=0){
            List<Passenger> currentPassengers = bus.getCurrentPassengers();
            Iterator<Passenger> currentPassengersIt = currentPassengers.iterator();
            while(currentPassengersIt.hasNext()){
                Passenger passenger = currentPassengersIt.next();
                if(passenger.getDestinationStop().equals(currentBusStop.getId())){
                    //到达目的站，乘客下车,累计乘客下车时间
                    durationOfPassengers +=passenger.getGetOffDuration();
                    //累计乘客数量
                    reducePassengersNumber++;
                    currentPassengersIt.remove();
                }
            }
        }
        //获取当前线路方向
        BusOperatingInLineStateEnum inLineState = bus.getInLineState();
        //获取在站点等候的乘客列表
        BlockingQueue<Passenger> waitingPassengers =null;
        switch (inLineState){
            case UP:
                waitingPassengers = currentBusStop.getWaitingInUpPassengers();
                break;
            case DOWN:
                waitingPassengers = currentBusStop.getWaitingInDownPassengers();
                break;
        }
        //乘客上车
        int addPassengersNumber = 0;
        while(true){
            //超过乘客数量不允许上车
            if(bus.getCurrentPassengers().size()>=SystemConstant.MAX_CAPACITY){
                break;
            }
            Passenger passenger = waitingPassengers.poll();
            if(passenger==null){
                break;
            }
            durationOfPassengers +=passenger.getGetInDuration();
            addPassengersNumber++;
            bus.getCurrentPassengers().add(passenger);
        }
        //更新总载客人数
        bus.setTotalPassengersNumbers(bus.getTotalPassengersNumbers()+addPassengersNumber);
        //更新上车乘客数量
        bus.setCurrentAddPassengersNumber(addPassengersNumber);
        //更新下车乘客数量
        bus.setCurrentReducePassengersNumber(reducePassengersNumber);
        //更新当前车厢人数
        bus.setCurrentPassengersNumber(bus.getCurrentPassengers().size());
        //更新乘客本次上下车耗时
        bus.setCurrentDurationOfPassengers(durationOfPassengers);
        //记录日志
        if(isLastStop){
            //增加终点站进站日志
            addBugLog(bus,now, currentBusStop, BusLogTypeEnum.LAST_STOP);
            System.out.println("公交进入终点站处理完成：" + bus.getName());
        }else{
            //增加公交进站日志
            addBugLog(bus,now, currentBusStop, BusLogTypeEnum.STOP);
            System.out.println("公交进站处理完成：" + bus.getName());
        }
       
    }

    /**
     * 增加公交车进站日志
     * @author 徐明龙 XuMingLong 2021-06-23
     * @param bus
     * @param now
     * @param currentBusStop
     * @param stop
     * @return void
     */
    private void addBugLog(OperatingBus bus, LocalDateTime now, OperatingBusStop currentBusStop, BusLogTypeEnum stop) {
        BusLog log = new BusLog();
        log.setBusId(bus.getId());
        log.setBusLineId(bus.getBusLineId());
        log.setNow(now);
        log.setDuration(Duration.between(begin,now).toMinutes());
        log.setStopId(currentBusStop.getId());
        log.setStopCode(currentBusStop.getCode());
        log.setStopName(currentBusStop.getName());
        log.setLogType(stop);
        log.setCurrentPassengersNumber(bus.getCurrentPassengersNumber());
        log.setAddPassengersNumber(bus.getCurrentAddPassengersNumber());
        log.setReducePassengersNumber(bus.getCurrentReducePassengersNumber());
        busLogMap.computeIfAbsent(bus.getId(), e -> new ArrayList<>(32))
            .add(log);
    }


    /**
     * 公交离站处理
     * @author 徐明龙 XuMingLong 2021-06-23
     * @param bus
     * @param now
     * @param isLastStop
     * @return void
     */
    private void processByBusDeparts(OperatingBus bus, LocalDateTime now, boolean isLastStop){
        //判断是为始发站
        boolean isFirstStop = isFirstStop(bus);
        //获取到当前站点
        OperatingBusStop currentBusStop = operatingBusLine.getOperatingBusStopMap().get(bus.getCurrentStopId());
        //获取当前线路方向
        BusOperatingInLineStateEnum inLineState = bus.getInLineState();
        //获取到下一个站点的时间
        long durationOfToNextStop = 0;
        int randomDelay = RandomUtils.nextInt(
            SystemConstant.MIN_DURATION_OF_RANDOM_UNDULATE,SystemConstant.MAX_DURATION_OF_RANDOM_UNDULATE+1
        );
        durationOfToNextStop +=randomDelay;
        switch (inLineState){
            case UP:
                //获取下一个站点id
                Integer idOfNextStop = currentBusStop.getIdOfNextStop();
                if(isLastStop){
                    //到达终点，需要掉头
                    bus.setInLineState(BusOperatingInLineStateEnum.DOWN);
                    //设置下一站为上一站
                    Integer idOfPreviousStop = currentBusStop.getIdOfPreviousStop();
                    bus.setNextStopId(idOfPreviousStop);
                    //获取到达下一站的时间
                    durationOfToNextStop = currentBusStop.getDurationOfToPreviousStop();
                    upOperatingBusMap.remove(bus.getId());
                    downOperatingBusMap.put(bus.getId(),bus);
                }else{
                    //设置下一站
                    OperatingBusStop nexBusStop = operatingBusLine.getOperatingBusStopMap().get(idOfNextStop);
                    bus.setCurrentStopId(idOfNextStop);
                    bus.setNextStopId(nexBusStop.getIdOfNextStop());
                    //获取到达下一站的时间
                    durationOfToNextStop = nexBusStop.getDurationOfToNextStop();
                }
                break;
            case DOWN:
                //获取下一个站点id
                Integer idOfPreviousStop = currentBusStop.getIdOfPreviousStop();
                if(isLastStop){
                    //到达起点，需要掉头
                    bus.setInLineState(BusOperatingInLineStateEnum.UP);
                    //设置下一站
                    bus.setNextStopId(currentBusStop.getIdOfNextStop());
                    //获取到达下一站的时间
                    durationOfToNextStop = currentBusStop.getDurationOfToNextStop();
                    downOperatingBusMap.remove(bus.getId());
                    upOperatingBusMap.put(bus.getId(),bus);
                }else{
                    //设置下一站
                    OperatingBusStop nexBusStop = operatingBusLine.getOperatingBusStopMap().get(idOfPreviousStop);
                    bus.setCurrentStopId(idOfPreviousStop);
                    bus.setNextStopId(nexBusStop.getIdOfPreviousStop());
                    //获取到达下一站的时间
                    durationOfToNextStop = nexBusStop.getDurationOfToPreviousStop();
                }
                break;
        }
        //判断是否为出发站
        if(isFirstStop){
            //记录出发站离站的日志
            addBugLog(bus,now,currentBusStop,BusLogTypeEnum.FIRST_DEPARTS);
            System.out.println("公交从始发站离站处理完成：" + bus.getName());
        }else{
            //增加公交离站日志
            addBugLog(bus,now, currentBusStop, BusLogTypeEnum.DEPARTS);
            System.out.println("公交离站处理完成：" + bus.getName());

        }

        //更新到达下一站的时间
        bus.setDurationOfToNextStop(durationOfToNextStop);

    }



    /**
     * 退出运营
     * @author 徐明龙 XuMingLong 2021-06-23
     * @return void
     */
    private void exit(){
        runFlag.set(false);
    }


    /**
     * 输出日志
     * @author 徐明龙 XuMingLong 2021-06-23
     * @return void
     */
    private void printLog() {
        //打印汇总日志
        printSummaryInfo();
        //输出随机挑选的两辆公交车的日志
        List<Integer> idList = new ArrayList<>( operatingBusLine.getOperatingBusMap().keySet());
        privateBusLog(CustomRandomUtils.getAndRemoveRandomItem(idList));
        privateBusLog(CustomRandomUtils.getAndRemoveRandomItem(idList));

    }
    /**
     * 打印指定公交车的日志
     * @author 徐明龙 XuMingLong 2021-06-23
     * @param busId
     * @return void
     */
    private void privateBusLog(Integer busId) {
        OperatingBus bus = operatingBusLine.getOperatingBusMap().get(busId);
        System.out.println("公交车" + bus.getName());
        System.out.println("----------------------------------------------------------");
        System.out.println("|时间 | 动作|");
        List<BusLog> logList = busLogMap.get(busId);
        if(!CollectionUtils.isEmpty(logList)){
            logList.forEach(r-> System.out.println(formatBusLog(r)));
        }
        System.out.println("----------------------------------------------------------");
    }

    /**
     * 格式化日志
     * @author 徐明龙 XuMingLong 2021-06-23
     * @param log
     * @return java.lang.String
     */
    private String formatBusLog(BusLog log){
        BusLogTypeEnum logTypeEnum = log.getLogType();
        String content = "";
        switch (logTypeEnum){
            case FIRST_DEPARTS:
                content =  String.format("从%s站发车，乘客%d人",log.getStopName(),log.getCurrentPassengersNumber());
                break;
            case STOP:
                content =  String.format("到达%s站",log.getStopName());
                break;
            case DEPARTS:
                content =  String.format("下客%d人，上客%d人，继续出发",log.getReducePassengersNumber(),log.getAddPassengersNumber());
                break;
            case LAST_STOP:
                content =  "到达终点站";
                break;
        }

        return String.format("|%s|%s|",formatSecondsToTime(log.getDuration()),content);

    }

    /**
     * 格式化秒为时分
     * @author 徐明龙 XuMingLong 2021-06-23
     * @param seconds
     * @return java.lang.String
     */
    private String formatSecondsToTime(long seconds){
        long hours = TimeUnit.SECONDS.toHours(seconds);
        long minutes = TimeUnit.SECONDS.toMinutes(seconds-hours*60*60);
        return String.format("%d:%02d",hours,minutes);
    }



    /**
     * 打印汇总日志
     * @author 徐明龙 XuMingLong 2021-06-23
     * @return void
     */
    private void printSummaryInfo() {
        System.out.println("----------------------------------------------------------");
        System.out.println("|公交车（NAME) | 总载客人数 | 总运行时间（分钟）| 总行驶时间（分钟）|");
        operatingBusLine.getOperatingBusMap().forEach((k,v)-> System.out.println(getBusSummaryInfo(v)));
        System.out.println("----------------------------------------------------------");

    }

    /**
     * 获取公交车汇总信息
     * @author 徐明龙 XuMingLong 2021-06-23
     * @param bus
     * @return java.lang.String
     */
    private String getBusSummaryInfo(OperatingBus bus){
        return String.format("|%s|%d|%d|%d|",bus.getName(),bus.getTotalPassengersNumbers(),
            bus.getTotalDurationOfOperating(),bus.getTotalDurationOfDriving());
    }

}
